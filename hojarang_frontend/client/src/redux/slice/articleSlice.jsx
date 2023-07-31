import { createSlice } from "@reduxjs/toolkit";
export const articleSlice = createSlice({
    name: "article",
    initialState : [],
    reducers : {
        createArticle : (state, action) => {
            state.push(action.payload)
        },
        updateArticle : (state, action) => {
            const { id, cate, category, title, content } = action.payload
            const existingArticle = state.find(article => article.id === id);
                existingArticle.category = category;
            if(existingArticle) {
                existingArticle.cate = cate;
                existingArticle.category = category;
                existingArticle.title = title;
                existingArticle.content = content;
            }
        },
       
    }

})
export const {createArticle, updateArticle} = articleSlice.actions;
export default articleSlice;