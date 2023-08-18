import React from 'react';

export const TextArea = ({ label, handleContent }) => {
  return (
    <div className="flex flex-col">
      <label className="mb-2 text-base text-black">{label}</label>
      <textarea
        className="bg-white py-2 px-3 border border-gray2 focus:outline-main2 resize-none h-64 rounded-lg"
        onChange={handleContent}
      ></textarea>
    </div>
  );
};

export default TextArea;
