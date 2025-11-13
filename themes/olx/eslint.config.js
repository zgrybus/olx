import { defineConfig } from 'eslint/config';
import { tanstackConfig } from '@tanstack/eslint-config';
import reactHooks from 'eslint-plugin-react-hooks';

export default defineConfig([
  tanstackConfig,
  reactHooks.configs.flat.recommended,
]);
