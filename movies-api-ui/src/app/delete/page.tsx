import styles from "./page.module.css";
import { DeleteMovie } from "./DeleteMovie"

export const dynamic = 'force-dynamic'

export default function Delete() {
        return (
            <>
            <DeleteMovie env={{ SERVER_URL: process.env.SERVER_URL }} />
            </>
        );
}
