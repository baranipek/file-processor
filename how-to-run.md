# File processor

## How to test

```sh
./gradlew test 
```

## How to run the app

```sh
./gradlew bootRun
```

## Example Request

```sh
curl --location --request POST 'http://localhost:8080/uploadFile' \
--form 'file=@"/Users/baran/Desktop/test.txt"'
```


