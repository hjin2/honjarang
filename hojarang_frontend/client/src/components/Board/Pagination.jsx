function Pagination({ total, limit, page, setPage }) {
  const numPages = Math.ceil(total / limit);

  return (
    <div>
      <nav className="flex justify-center items-center gap-4 m-16">
        <button onClick={() => setPage(page - 1)} disabled={page === 1}>
          &lt;
        </button>
        {Array(numPages)
          .fill()
          .map((_, i) => (
            <button
              key={i + 1}
              onClick={() => setPage(i + 1)}
              aria-current={page === i + 1 ? "page" : null}
            >
              {i + 1}
            </button>
          ))}
        <buitton onClick={() => setPage(page + 1)} disabled={page === numPages}>
          &gt;
        </buitton>
      </nav>
    </div>
  );
}


export default Pagination;
