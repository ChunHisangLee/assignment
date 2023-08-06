FROM ubuntu:latest
LABEL authors="mds"

ENTRYPOINT ["top", "-b"]