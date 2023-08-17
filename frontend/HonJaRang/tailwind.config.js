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
        main5: "#FF7D1F",
        gray1: "#E3E3E3",
        gray2: "#BEBEBE",
        gray3: "#888888",
        gray4: "#676767",
        gray5: "#525252",
      },
      animation:{
        'pulse' : 'pulse 5s cubic-bezier(0.25, 0.25, 0.75, 0.75)'
      },
      fontFamily:{
        ImcreSoojin : ["ImcreSoojin"],
        omyu_pretty : ["omyu_pretty"]

      }
    },
  },
  plugins: [require('tailwind-scrollbar-hide')],
}