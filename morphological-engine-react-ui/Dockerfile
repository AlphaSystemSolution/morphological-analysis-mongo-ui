FROM node:14.15.1-alpine3.11

# environment variables
ENV NODE_ENV=production
ENV REACT_APP_MORPHOLOGICAL_ENGINE_HOST=http://localhost:8080

# install serve
RUN npm install -g serve

# set working directory
WORKDIR /app

# add app
COPY build ./build

EXPOSE 5000

# start app
CMD ["serve", "-s", "build"]
