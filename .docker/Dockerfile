FROM amazoncorretto:21-alpine3.19

ARG EXPOSE_PORT=8080
ARG USER_ID=1000
ARG USERNAME=java21
ARG APP_ROOT_PATH=/app

RUN apk update && apk upgrade \
    && apk add --no-cache git openssh-client \
    && rm -rf /var/cache/apk/* \
    && addgroup -g $USER_ID $USERNAME \
    && adduser -D -G $USERNAME -u $USER_ID $USERNAME

USER ${USERNAME}

WORKDIR ${APP_ROOT_PATH}

EXPOSE ${EXPOSE_PORT}

ENTRYPOINT [ "sh" ]

CMD [ "./.docker/post-start-command.sh" ]
