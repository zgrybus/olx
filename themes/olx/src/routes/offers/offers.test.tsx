import { screen, within } from '@testing-library/react';
import { HttpResponse, delay } from 'msw';
import userEvent from '@testing-library/user-event';
import { renderApp } from '@/test/test-utils';
import { mswServer } from '@/test/msw/msw-server';
import { olxHttpMsw } from '@/apis/olx/msw';
import { getMockOffersList } from '@/apis/olx/mocks/mock-offers-list';

describe('Offers Page', () => {
  beforeEach(() => {
    mswServer.use(
      olxHttpMsw.get('/api/offers', () =>
        HttpResponse.json(getMockOffersList({ count: 3 })),
      ),
    );
  });

  test('renders skeleton, when we fetch offers list', async () => {
    mswServer.use(olxHttpMsw.get('/api/offers', () => delay('infinite')));

    await renderApp({ to: '/offers' });

    expect(screen.getByTestId('offers-list-skeleton')).toBeVisible();

    expect(screen.queryByRole('heading')).not.toBeInTheDocument();
    expect(
      screen.queryByRole('list', { name: 'offers list' }),
    ).not.toBeInTheDocument();
  });

  describe('Display', () => {
    test('renders offers', async () => {
      await renderApp({ to: '/offers' });

      expect(
        await screen.findByRole('heading', { name: 'We found 3 offers' }),
      ).toBeVisible();
      expect(
        screen.queryByTestId('offers-list-skeleton'),
      ).not.toBeInTheDocument();

      const offersList = screen.getByRole('list', { name: 'offers list' });
      expect(offersList).toBeVisible();

      const offers = within(offersList).getAllByRole('link');
      expect(offers).toHaveLength(3);

      expect(offers[0]).toHaveTextContent(
        'title_0PLN 1,000.00id_0Updated: June 5, 2025Created: June 5, 2024',
      );
      expect(offers[0]).toHaveAttribute('href', '/offers/0');

      expect(offers[1]).toHaveTextContent(
        'title_1PLN 999.00id_1Updated: June 5, 2024Created: June 5, 2023',
      );
      expect(offers[1]).toHaveAttribute('href', '/offers/1');

      expect(offers[2]).toHaveTextContent(
        'title_2PLN 998.00id_2Updated: June 5, 2023Created: June 5, 2022',
      );
      expect(offers[2]).toHaveAttribute('href', '/offers/2');
    });
  });

  describe('Navigation', () => {
    test('redirects to offer details, when we click on offer item', async () => {
      const user = userEvent.setup();
      const { router } = await renderApp({ to: '/offers' });

      const offersList = await screen.findByRole('list', {
        name: 'offers list',
      });
      expect(offersList).toBeVisible();

      const offers = within(offersList).getAllByRole('link');

      expect(router.state.location.href).toBe('/offers');

      await user.click(offers[0]);

      expect(router.state.location.href).toBe('/offers/0');
    });
  });
});
