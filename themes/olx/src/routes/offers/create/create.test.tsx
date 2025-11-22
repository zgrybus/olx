import { HttpResponse } from 'msw';
import userEvent from '@testing-library/user-event';
import { screen } from '@testing-library/react';
import type { MswRequest } from '@/test/types';
import { renderApp } from '@/test/test-utils';
import { mswServer } from '@/test/msw/msw-server';
import { olxHttpMsw } from '@/apis/olx/msw';

describe('Offers Page', () => {
  let createOfferMsw: Array<MswRequest> = [];

  beforeEach(() => {
    createOfferMsw = [];

    mswServer.use(
      olxHttpMsw.post('/api/offers', ({ request }) => {
        createOfferMsw.push(request);
        return HttpResponse.json();
      }),
    );
  });

  test('creates an offer and redirects to offers page', async () => {
    const title = 'IKEA Friheten Corner Sofa Be';
    const description =
      'Dark gray corner sofa bed from IKEA (Friheten model). It converts easily into a double bed and has large storage space underneath.';
    const price = 250;

    const user = userEvent.setup();
    const { router } = await renderApp({ to: '/offers/create' });

    await user.type(
      screen.getByRole('textbox', { name: 'Title of the offer' }),
      title,
    );
    await user.type(
      screen.getByRole('textbox', { name: 'Description' }),
      description,
    );
    await user.type(
      screen.getByRole('spinbutton', { name: 'Price' }),
      `${price}`,
    );

    expect(createOfferMsw).toHaveLength(0);
    expect(router.state.location.href).toBe('/offers/create');

    await user.click(screen.getByRole('button', { name: 'Add Offer' }));

    expect(
      await screen.findByText(/Your offer is now live and visible to others/i),
    ).toBeVisible();

    expect(createOfferMsw).toHaveLength(1);
    expect(await createOfferMsw[0].json()).toEqual({
      title,
      description,
      price,
    });

    expect(router.state.location.href).toBe('/offers');
  });
});
