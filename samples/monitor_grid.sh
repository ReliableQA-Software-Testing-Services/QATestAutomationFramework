#!/bin/bash

# Threshold percentage
THRESHOLD=80

echo "Monitoring Selenium Grid Resources... (Press Ctrl+C to stop)"
echo "----------------------------------------------------------"

while true; 
	do
    # Get docker stats for selenium containers without the streaming UI
    docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}" | grep "chrome\|firefox\|hub" | while read -r line; 
		do
			# Extract CPU percentage (removing the % sign for comparison)
			CPU_VAL=$(echo $line | awk '{print $2}' | sed 's/%//' | cut -d. -f1)
			NAME=$(echo $line | awk '{print $1}')

			if [ "$CPU_VAL" -gt "$THRESHOLD" ]; then
				echo "WARNING: Container [$NAME] is at ${CPU_VAL}% CPU! Tests may timeout."
			fi
		done
    sleep 5
	done