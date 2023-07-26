/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors:{
        main1: "#008B57",
        main2: "#88B451",
        main3: "#FFD57E",
        main4: "#B8DA8D",
        gray1: "#E3E3E3",
        gray2: "#BEBEBE",
        gray3: "#888888",
        gray4: "#676767",
        gray5: "#525252",
      }
    },
  },
  plugins: [],
}