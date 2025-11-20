import { HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import { olxHttpMsw } from '@/apis/olx/msw';

export const mswServer = setupServer(
  olxHttpMsw.get('/api/offers', () => HttpResponse.json([])),
  olxHttpMsw.get('/api/offers/{offerId}', () => HttpResponse.json(null)),
);
