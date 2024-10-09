import styles from "./page.module.css";
import SearchMovie from "./SearchMovie"

export const dynamic = 'force-dynamic'

export default function Search() {

        return (
            <>
            <SearchMovie env={{ SERVER_URL: process.env.SERVER_URL }} />
            </>
        );
}