# install NodeJS Version 21
FROM node:21-alpine as build

 # Create app folder
WORKDIR /app

# Copy package.json to app folder
COPY package*.json /app/

# Install ionic and other dependencies of package.json
RUN npm install -g ionic
RUN npm install

# Copy project from local device to Container
COPY ./ /app/

# Execute prod build
RUN npm run build

# Install nginx webserver
FROM nginx:alpine

# Remove default HTML files from html folder
RUN rm -rf /usr/share/nginx/html/*

# Copy build output to nginx html folder
COPY --from=build /app/www/ /usr/share/nginx/html/

# Use port 80 for app
EXPOSE 80
