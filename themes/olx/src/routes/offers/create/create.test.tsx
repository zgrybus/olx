import { HttpResponse } from 'msw';
import userEvent from '@testing-library/user-event';
import { screen } from '@testing-library/react';
import { QueryClient } from '@tanstack/react-query';
import { CreateOfferPage } from '.';
import type { MswRequest } from '@/test/types';
import {
  renderApp,
  renderComponentWithRouterAndProviders,
} from '@/test/test-utils';
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

  test('creates an offer, invalidates offers and redirects to offers page', async () => {
    const queryClient = new QueryClient();
    const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');

    const title = 'IKEA Friheten Corner Sofa Be';
    const description =
      'Dark gray corner sofa bed from IKEA (Friheten model). It converts easily into a double bed and has large storage space underneath.';
    const price = 250;

    const user = userEvent.setup();
    const { router } = await renderApp(
      { to: '/offers/create' },
      { queryClient },
    );

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

    expect(invalidateQueriesSpy).toHaveBeenCalledWith({
      queryKey: [['get', '/api/offers']],
    });
    expect(router.state.location.href).toBe('/offers');
  });

  describe('Validation', () => {
    const getText = (len: number) =>
      Array.from({ length: len }, () => '0').join('');

    describe('Title', () => {
      test('validates min length', async () => {
        const title = getText(14);
        const user = userEvent.setup();
        await renderComponentWithRouterAndProviders(<CreateOfferPage />);

        await user.type(
          screen.getByRole('textbox', { name: 'Title of the offer' }),
          title,
        );

        await user.click(screen.getByRole('button', { name: 'Add Offer' }));
        expect(screen.getByText('14/70')).toBeVisible();
        expect(
          screen.getByText('The title is too short. Please add more details.'),
        ).toBeVisible();
      });

      test('validates max length', async () => {
        const title = getText(71);
        const user = userEvent.setup();
        await renderComponentWithRouterAndProviders(<CreateOfferPage />);

        await user.type(
          screen.getByRole('textbox', { name: 'Title of the offer' }),
          title,
        );

        await user.click(screen.getByRole('button', { name: 'Add Offer' }));
        expect(screen.getByText('71/70')).toBeVisible();
        expect(
          screen.getByText(`The title is too long. Don't add so much detail.`),
        ).toBeVisible();
      });
    });

    describe('Description', () => {
      test('validates min length', async () => {
        const description = getText(39);
        const user = userEvent.setup();
        await renderComponentWithRouterAndProviders(<CreateOfferPage />);

        await user.type(
          screen.getByRole('textbox', { name: 'Description' }),
          description,
        );

        await user.click(screen.getByRole('button', { name: 'Add Offer' }));
        expect(screen.getByText('39/9000')).toBeVisible();
        expect(
          screen.getByText(
            'The description is too short. Please add more details.',
          ),
        ).toBeVisible();
      });
    });

    describe('Price', () => {
      test('validates min number', async () => {
        const price = -1;
        const user = userEvent.setup();
        await renderComponentWithRouterAndProviders(<CreateOfferPage />);

        await user.type(
          screen.getByRole('spinbutton', { name: 'Price' }),
          `${price}`,
        );

        await user.click(screen.getByRole('button', { name: 'Add Offer' }));
        expect(screen.getByText('Price must be greater than 0')).toBeVisible();
      });

      test('validates number type', async () => {
        const price = 'price';
        const user = userEvent.setup();
        await renderComponentWithRouterAndProviders(<CreateOfferPage />);

        await user.type(
          screen.getByRole('spinbutton', { name: 'Price' }),
          `${price}`,
        );

        await user.click(screen.getByRole('button', { name: 'Add Offer' }));
        expect(screen.getByText('Price must be greater than 0')).toBeVisible();
      });
    });
  });
});
