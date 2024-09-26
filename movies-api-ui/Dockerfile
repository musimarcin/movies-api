# Stage 1: Install dependencies
FROM node:16-alpine AS deps
RUN apk add --no-cache libc6-compact
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci

# Stage 2: Build the application
FROM node:16-alpine AS builder
WORKDIR /app
COPY .. .
COPY --from=deps /app/node_modules ./node_modules
RUN npm run build

# Stage 3: Create the production image
FROM node:16-alpine AS runner
WORKDIR /app
ENV NODE_ENV production

# Create a non-root user
RUN addgroup -g 1001 -S nodejs
RUN adduser -S nextjs -u 1001

# Copy necessary files from the builder stage
COPY --from=builder /app/next.config.js ./ 
COPY --from=builder /app/public ./public 
COPY --from=builder --chown=nextjs:nodejs /app/.next ./.next 
COPY --from=builder /app/node_modules ./node_modules 
COPY --from=builder /app/package.json ./package.json 

# Switch to the non-root user
USER nextjs

# Expose the port the app runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
