/**
 * 
 */
package com.elastica.beatle.replayTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.LogReplayConstant;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class EPDV1Parser {

	private enum RequestType {
		GET, POST, DELETE, PUT, OPTIONS;

		public static boolean contains(String value) {
			for (RequestType type : RequestType.values())
				if (type.name().equals(value))
					return true;

			return false;
		}
	};

	private enum ResponseType {
		HTTP11("HTTP/1.1"), HTTP10("HTTP/1.0"), UNKNOWN("unknown");
		private final String Type;

		private ResponseType(String type) {
			this.Type = type;
		}

		@Override
		public String toString() {
			return Type;
		}

		public static boolean contains(String value) {
			for (ResponseType type : ResponseType.values()) {
				if (type.Type.equals(value))
					return true;
			}
			return false;
		}
	};

	public Map<LogReplayRequestDTO, LogReplayReponseDTO> readRequestsFromFile(
			String logFilePath) throws FileNotFoundException, IOException,
			URISyntaxException {
		String completeFilePath = FileHandlingUtils
				.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH
						+ logFilePath);
		Logger.info("Processind data from :" + completeFilePath);
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(completeFilePath))));
		String currentProcessingLine = null;
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestMap = new HashMap<>();

		while ((currentProcessingLine = bufferReader.readLine()) != null) {
			if (!currentProcessingLine.matches("")) {
				Logger.info("Started prcessing and collecting data for :"
						+ currentProcessingLine);
				String[] requestLine = currentProcessingLine.split(" ");
				LogReplayRequestDTO request = new LogReplayRequestDTO();
				LogReplayReponseDTO response = new LogReplayReponseDTO();
				if (RequestType.contains(requestLine[0])) {
					request.setMethodType(requestLine[0]);
					request.setRequestURI(new URI(requestLine[1]));
					request.setHttpRequestVersion(requestLine[2]);
					List<NameValuePair> requestHeader = new ArrayList<>();
					while (!(currentProcessingLine = bufferReader.readLine())
							.matches("")) {
						String[] headerLine = currentProcessingLine.split(":");
						requestHeader.add(new BasicNameValuePair(headerLine[0]
								.trim(), headerLine[1].trim()));
					}
					request.setRequestHeader(requestHeader);
					if (request.getMethodType().equals("GET")) {
						if (currentProcessingLine.matches(""))
							currentProcessingLine = bufferReader.readLine();
					} else if (request.getMethodType().equals("POST")
							|| request.getMethodType().equals("PUT")) {
						request.setRequestBody(bufferReader.readLine());
						currentProcessingLine = bufferReader.readLine();
					}

					currentProcessingLine = bufferReader.readLine();
					if (ResponseType
							.contains(currentProcessingLine.split(" ")[0])) {

						response.setResponseStatus(currentProcessingLine);
						List<NameValuePair> responseHeader = new ArrayList<>();
						while (!(currentProcessingLine = bufferReader
								.readLine()).matches("")) {
							String[] respheaderLine = currentProcessingLine
									.split(":");
							responseHeader.add(new BasicNameValuePair(
									respheaderLine[0].trim(), respheaderLine[1]
											.trim()));
						}
						response.setResponseHeader(responseHeader);
						currentProcessingLine = bufferReader.readLine();
						if (!currentProcessingLine.matches(""))
							response.setResponseBody(currentProcessingLine);
					}
					requestMap.put(request, response);
				} else
					Logger.error("Unknow request found");
			}
		}
		bufferReader.close();
		return requestMap;
	}
}
