import React from "react";
import Article from "./Article";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

export default function AricleList(){
    const navigate = useNavigate();
    const articleCreate = () => {
        navigate('./ArticleCreate');
    }

    return(
        <div>
            <Article/>
            <Link to='/board/articlecreate'>
                <button onClick={articleCreate}>작성하기</button>
            </Link>
        </div>
    )
}