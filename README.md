# File Storage application

## Required Prerequisites

* JDK 8

## Building and Running

```
./mvnw  package spring-boot:run
```

## Usage Instructions

FileControllerTest.java  can be taken as an example how to call application endpoints

### File Uploading

```
curl -v -F file=@/<file_path> localhost:8080/file
```
e.g.:
```
curl -v -F file=@/Users/user/Downloads/test.txt localhost:8080/file
```
Response payload  contains token field that should be used for further file access

### File uploading with new version
```
curl -v -H X-Token:<token_value> -F file=@/<file_path> localhost:8080/file
```
where <token_value> is taken from file uploading response

e.g.:

```
curl -v -H X-Token:GUEGRvTt -F file=@/Users/user/Downloads/test.txt localhost:8080/file
```

### File downloading (with latest file version)

```
curl -v -H X-Token:<token_value> localhost:8080/file/<filename>
```
where <token_value> is taken from file uploading response

e.g.:
```
curl -v -H X-Token:GUEGRvTt localhost:8080/file/test.txt/body
```

### File downloading with certain file version

```
curl -v -H X-Token:<token_value> localhost:8080/file/<filename>?version=<version_number>
```
where <token_value> is taken from file uploading response,
<version_number> = requested version number

e.g.:
```
curl -v -H X-Token:GUEGRvTt localhost:8080/file/test.txt/body?version=1
```

