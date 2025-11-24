import { defineConfig } from 'eslint/config';
import { tanstackConfig } from '@tanstack/eslint-config';
import reactHooks from 'eslint-plugin-react-hooks';
import reactPlugin from 'eslint-plugin-react';
import jsxA11y from 'eslint-plugin-jsx-a11y';
import tseslint from 'typescript-eslint';
import vitest from 'eslint-plugin-vitest';
import testingLibrary from 'eslint-plugin-testing-library';
import eslintPluginBetterTailwindcss from 'eslint-plugin-better-tailwindcss';

export default defineConfig([
  {
    ignores: [
      'dist',
      'node_modules',
      'coverage',
      '**/*.config.js',
      '**/*.config.ts',
    ],
  },
  {
    settings: {
      react: {
        version: 'detect',
      },
    },
  },

  ...tseslint.configs.recommended,
  tanstackConfig,
  reactPlugin.configs.flat.recommended,
  reactPlugin.configs.flat['jsx-runtime'],
  reactHooks.configs.flat.recommended,
  jsxA11y.flatConfigs.recommended,
  {
    rules: {
      '@typescript-eslint/consistent-type-definitions': ['error', 'type'],
      'react/no-children-prop': ['off'],
    },
  },
  {
    files: ['**/*.{jsx,tsx}'],
    languageOptions: {
      parserOptions: {
        ecmaFeatures: {
          jsx: true,
        },
      },
    },
    plugins: {
      'better-tailwindcss': eslintPluginBetterTailwindcss,
    },
    rules: {
      ...eslintPluginBetterTailwindcss.configs['recommended-warn'].rules,
      ...eslintPluginBetterTailwindcss.configs['recommended-error'].rules,
    },
    settings: {
      'better-tailwindcss': {
        entryPoint: 'src/styles.css',
        tailwindConfig: 'tailwind.config.js',
      },
    },
  },
  {
    files: ['src/**/*.{test,spec}.{ts,tsx}'],
    plugins: {
      vitest,
      'testing-library': testingLibrary,
    },
    languageOptions: {
      globals: {
        ...vitest.environments.env.globals,
      },
    },
    rules: {
      ...vitest.configs.recommended.rules,
      ...testingLibrary.configs['flat/react'].rules,
    },
  },
]);
