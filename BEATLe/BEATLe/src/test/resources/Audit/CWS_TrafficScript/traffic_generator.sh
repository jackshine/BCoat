#!/bin/bash

array=("https://www.salesforce.com" "https://www.dropbox.com" "https://www.box.com" "https://twitter.com" "https://secure.logmein.com/")
successCount=0
failureCount=0
for url in "${array[@]}"; do
    Response=$(curl --write-out %{http_code} --silent --output /dev/null $url -x access801.cws.sco.cisco.com:8080)
    if [ "$Response" = "200" ]; then
        echo "Success: "$url" Status Code: "$Response
        ((successCount++))
    elif [ "$Response" = "403"] || [ "$Response" = "000"]; then
        echo "Failure. You need to register your IP with Cisco"$Response
        ((failureCount++))
    else
        echo "Failed: "$url" Status Code: "$Response
        ((failureCount++))
    fi

    sleep 25
done

echo "=========================================="
echo "Traffic creation status: "
echo "=========================================="
echo "Total Number of Curl calls: "${#array[@]}
echo "Successful curl calls: "$successCount
echo "Failure curl calls: "$failureCount