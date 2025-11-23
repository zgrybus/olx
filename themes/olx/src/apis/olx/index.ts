import createFetchClient from 'openapi-fetch';
import createClient from 'openapi-react-query';
import type { paths } from './generated/client';

const fetchClient = createFetchClient<paths>({
  baseUrl: import.meta.env.VITE_OLX_SERVICE_URL,
  fetch: (...args) => fetch(...args),
});

export const $olxApi = createClient(fetchClient);
