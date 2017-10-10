/**
 * 
 */
package com.elastica.beatle.replayTool;

import java.util.concurrent.TimeUnit;

/**
 * @author anuvrath
 *
 */
public class MagicCookie {
	private int[] secret_primes = { 5051, 6113, 7243, 7759, 3191, 4139, 7901 };
	private long day;
	private long today;
	private long magic_cookie;

	/**
	 * To bypass portal authentication, can insert magic-cookie Header.
	 * @return ts*(first)*(second)+ts*ts
	 */
	public long getMagicCookie() {
		day = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())/ (24 * 3600);
		if (today == day)
			return magic_cookie;
		else {
			magic_cookie = day
					* secret_primes[(int) (day % secret_primes.length)]
					* secret_primes[(int) ((day + 3) % secret_primes.length)]
					+ day * day;
			today = day;
			return magic_cookie;
		}
	}		
}
