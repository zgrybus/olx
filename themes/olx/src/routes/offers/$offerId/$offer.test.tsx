import { screen, waitFor } from '@testing-library/react';
import { HttpResponse, delay } from 'msw';
import userEvent from '@testing-library/user-event';
import type { MswRequest } from '@/test/types';
import { renderApp } from '@/test/test-utils';
import { mswServer } from '@/test/msw/msw-server';
import { olxHttpMsw } from '@/apis/olx/msw';
import { getMockOfferDetails } from '@/apis/olx/mocks/mock-offer-details';

describe('Offers Page', () => {
  let offerDetailsMsw: Array<MswRequest> = [];

  beforeEach(() => {
    offerDetailsMsw = [];

    mswServer.use(
      olxHttpMsw.get('/api/offers/{offerId}', ({ request }) => {
        offerDetailsMsw.push(request);
        return HttpResponse.json(getMockOfferDetails());
      }),
    );
  });

  test('calls offer details with offer id from the url', async () => {
    await renderApp({ to: '/offers/$offerId', params: { offerId: '120' } });

    await waitFor(() => expect(offerDetailsMsw).toHaveLength(1));

    expect(offerDetailsMsw[0].url).toContain('/api/offers/120');
  });

  test('renders skeleton, when we fetch offers list', async () => {
    mswServer.use(
      olxHttpMsw.get('/api/offers/{offerId}', () => delay('infinite')),
    );

    await renderApp({ to: '/offers/$offerId', params: { offerId: '120' } });

    expect(screen.getByTestId('offer-details-skeleton')).toBeVisible();

    expect(
      screen.queryByTestId('offer-details-summary-card'),
    ).not.toBeInTheDocument();
    expect(
      screen.queryByTestId('offer-details-description-card'),
    ).not.toBeInTheDocument();
  });

  describe('Display', () => {
    test('renders offers', async () => {
      await renderApp({ to: '/offers/$offerId', params: { offerId: '120' } });

      expect(
        await screen.findByTestId('offer-details-description-card'),
      ).toBeVisible();

      expect(
        screen.getByTestId('offer-details-summary-card'),
      ).toHaveTextContent(
        'Added September 12, 2025title_details_120PLN 5,000.00',
      );
      expect(
        screen.getByTestId('offer-details-description-card'),
      ).toHaveTextContent('Descriptiondescription_details_120ID: 120');

      expect(
        screen.queryByTestId('offer-details-skeleton'),
      ).not.toBeInTheDocument();
    });
  });

  describe('Navigation', () => {
    test('redirects to offer details, when we click on offer item', async () => {
      const user = userEvent.setup();
      const { router } = await renderApp({
        to: '/offers/$offerId',
        params: { offerId: '120' },
      });

      expect(router.state.location.href).toBe('/offers/120');

      await user.click(screen.getByRole('link', { name: 'Go back' }));

      expect(router.state.location.href).toBe('/offers');
    });
  });
});
