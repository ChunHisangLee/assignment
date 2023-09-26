FROM ubuntu:latest

LABEL maintainer="jack"

ENTRYPOINT ["top", "-b"]
