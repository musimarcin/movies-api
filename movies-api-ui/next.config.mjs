/** @type {import('next').NextConfig} */
const nextConfig = {
    serverRuntimeConfig: {
        API_URL: process.env.SERVER_URL
      },
      publicRuntimeConfig: {
        API_URL: process.env.CLIENT_URL
      }
};

export default nextConfig;
