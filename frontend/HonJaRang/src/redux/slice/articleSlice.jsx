import { createSlice } from '@reduxjs/toolkit';

// const initialState = {
//   articles: [],
// }

export const articleSlice = createSlice({
  name: 'article',
  initialState: [],
  reducers: {
    createArticle: (state, action) => {
      // const newArticle = action.payload;
      // state.articles = [...state.articles, newArticle];
      state.push(action.payload);
    },
    updateArticle: (state, action) => {
      const { id, category, title, content } = action.payload;
      const existingArticle = state.find((article) => article.id === id);
      if (existingArticle) {
        existingArticle.category = category;
        existingArticle.category = category;
        existingArticle.title = title;
        existingArticle.content = content;
      }
    },
    deleteArticle: (state, action) => {
      const { id } = action.payload;

      const existingArticle = state.find((article) => article.id === id);
      if (existingArticle) {
        return state.filter((article) => article.id != id);
      }
    },
  },
});
export const { createArticle, updateArticle, deleteArticle } =
  articleSlice.actions;
export default articleSlice;
