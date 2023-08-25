#!/bin/bash

# MySQL server details
host="ec2-13-126-198-67.ap-south-1.compute.amazonaws.com"
port="3306"
user="pradip"
password="PradipAws@123"
database="room"
backup_dir="/home/root320/Desktop/Projects/Room-Project/ProdDatabaseBackup"
datetime=$(date +"%Y%m%d_%H%M%S")
backup_file="$backup_dir/${database}_${datetime}.sql"

# Create backup directory if it doesn't exist
mkdir -p "$backup_dir"

# Perform the backup
mysqldump -h "$host" -P "$port" -u "$user" -p"$password" "$database" > "$backup_file"

# Check if mysqldump command was successful
if [ $? -eq 0 ]; then
  echo "Backup created: $backup_file"
else
  echo "Backup failed."
fi

