/** @type {import('next').NextConfig} */
const nextConfig = {
    async redirects() {
        return [
          {
            source: '/',
            destination: '/movies',
            permanent: true
          }
        ]
    }
};

export default nextConfig;
