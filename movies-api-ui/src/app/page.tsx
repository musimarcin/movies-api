import HomeMovie from './HomeMovie';

export const dynamic = 'force-dynamic'

export default function Home() {

    return (
        <>
        <HomeMovie env={{ SERVER_URL: process.env.SERVER_URL }} />
        </>
    );
}
