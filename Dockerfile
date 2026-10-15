FROM ubuntu:latest
LABEL authors="eduardo"

ENTRYPOINT ["top", "-b"]