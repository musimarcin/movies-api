import styles from "./page.module.css";
import AddMovie from "./AddMovie"

export const dynamic = 'force-dynamic'

export default function Add() {

    return (
        <>
        <AddMovie env={{ SERVER_URL: process.env.SERVER_URL }} />
        </>
    );
}


