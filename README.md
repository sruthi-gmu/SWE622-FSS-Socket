# SWE622-FSS-Socket
Instructions

Start server
----------------------
java -jar pa1.jar server start 8000 &


Upload a file to the server
---------------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client upload <path_on_client> <path_on_server>


Download file from server
-------------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client download <path_on_server> <path_on_client>

Create directory on server
--------------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client mkdir <path_on_server>

list the directories/files
--------------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client dir <path_of_directory_on_server>

remove directory on the server
------------------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client rmdir <path_of_directory_on_server>

remove file on the server
-------------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client rm <path_of_file_on_server>

shutdown the server
-------------------
export PA1_SERVER="localhost:8000"
java -jar pa1.jar client shutdown
