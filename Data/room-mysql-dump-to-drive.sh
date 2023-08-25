#!/bin/bash

# MySQL server details
host="localhost"
port="3306"
user="pradip"
password="PradipAws@123"
database="room"
backup_dir="/home/root320/Desktop/Projects/Room-Project/ProdDatabaseBackup"
datetime=$(date +"%d-%m-%Y_%H:%M")
file_name="${database}_dump_${datetime}.sql"
backup_file="$backup_dir/${file_name}"

# Replace these values with your actual credentials
CLIENT_ID="475315782228-2kocg3lsa4q82mvpu2kdnvjoimou8b5j.apps.googleusercontent.com"
CLIENT_SECRET="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"
REFRESH_TOKEN="1//0gdMn6MnIPCRHCgYIARAAGBASNwF-L9IrVhFiAe3cLT_dCC8ets-LRi3tbDV36Dx7YHFPRHf3UyU2KFZKLn7TZL2Xp2jL5qLmnqM"
DRIVE_FOLDER_ID="1OAtPxmhe4i9jflYQVcom3qUdl2B6inNg"

# Create backup directory if it doesn't exist
mkdir -p "$backup_dir"

echo "$backup_file"

# Perform the backup
mysqldump -h "$host" -P "$port" -u "$user" -p"$password" "$database" > "$backup_file"

# Check if mysqldump command was successful
if [ $? -eq 0 ]; then
  	echo "Backup created: $backup_file"

	# Perform the POST request to get the access token
	response=$(curl -s --request POST --data "client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&refresh_token=$REFRESH_TOKEN&grant_type=refresh_token" https://oauth2.googleapis.com/token)
	# Extract the access token using jq
	access_token=$(echo "$response" | grep -o '"access_token": *"[^"]*"' | awk -F'"' '{print $4}')
	
	# Check if access token extraction was successful
	if [ -z "$access_token" ]; then
	    echo "Error extracting access token"
	    exit 1
	fi

   	# Upload to Google Drive
	upload_response=$(curl --location 'https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart' \
	--header "Authorization: Bearer $access_token" \
	--form "metadata={name:'${file_name}', parents:['${DRIVE_FOLDER_ID}']};type=application/json" \
	--form 'file=@"'$backup_file'"')

	# Check if upload was successful
	if echo "$upload_response" | grep -q '"id"'; then
	    echo "Backup uploaded to Google Drive."
	else
	    echo "Backup upload to Google Drive failed."
	fi

else
  echo "Backup failed."
fi

