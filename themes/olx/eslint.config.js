import { defineConfig } from 'eslint/config';
import { tanstackConfig } from '@tanstack/eslint-config';
import reactHooks from 'eslint-plugin-react-hooks';
import reactPlugin from 'eslint-plugin-react';
import jsxA11y from 'eslint-plugin-jsx-a11y';
import tseslint from 'typescript-eslint';

export default defineConfig([
  tseslint.configs.recommended,
  tanstackConfig,
  reactPlugin.configs.flat.recommended,
  reactPlugin.configs.flat['jsx-runtime'],
  reactHooks.configs.flat.recommended,
  jsxA11y.flatConfigs.recommended,
  {
    rules: {
      '@typescript-eslint/consistent-type-definitions': ['error', 'type'],
    },
  },
]);
