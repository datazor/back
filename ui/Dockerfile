# Stage 1: Build Stage
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json separately to leverage Docker caching
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Build the application
RUN npm run build

# Stage 2: Production Stage
FROM nginx:alpine

# Copy build output from the builder stage to the nginx HTML directory
COPY --from=builder /app/dist/kanban-ui/* /usr/share/nginx/html/

# Add a custom nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 for the application
EXPOSE 80

# Start nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
