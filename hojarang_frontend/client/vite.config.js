import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import {resolve} from 'node:path'


// https://vitejs.dev/config/
export default defineConfig({
  define:{
    'global': "window",
  },
  plugins: [react()],
  resolve: {
    alias: {
      util: 'util/',
      '@': resolve(__dirname, 'src'),
    },
  },
  preview:{
    host: true,
    port: 3000,
  }
})
