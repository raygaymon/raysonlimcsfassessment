export interface News {
    title: string,
    postDate: number,
    description: string,
    tags: string[],
    imageUrl: string
}

export interface TagCount {
    tag: string;
    count: number
}