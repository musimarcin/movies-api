export interface MovieResponse {
    data: Movie[],
    totalElements: number,
    currentPage: number,
    totalPages: number,
    isFirst: boolean,
    isLast: boolean,
    hasNext: boolean,
    hasPrevious: boolean
}

export interface Movie {
    id: number,
    title: string,
    releaseYear: number,
    created_at: Date
}