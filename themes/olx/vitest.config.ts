import { URL, fileURLToPath } from 'node:url';
import { defineConfig } from 'vitest/config';
import viteReact from '@vitejs/plugin-react';

// https://vitest.dev/config/
export default defineConfig({
  plugins: [viteReact()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  test: {
    globals: true,
    environment: 'happy-dom',
    setupFiles: ['./src/test/setup.ts'],
    css: false,
  },
});
