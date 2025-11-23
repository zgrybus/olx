import { QueryClient } from '@tanstack/react-query';
import userEvent from '@testing-library/user-event';
import { screen } from '@testing-library/react';
import { HttpResponse } from 'msw';
import { RemoveOfferDialog } from './remove-offer-dialog';
import type { MswRequest } from '@/test/types';
import {
  renderApp,
  renderComponentWithRouterAndProviders,
} from '@/test/test-utils';
import { mswServer } from '@/test/msw/msw-server';
import { olxHttpMsw } from '@/apis/olx/msw';

describe('Remove offer dialog', () => {
  let removeOfferMsw: Array<MswRequest> = [];

  beforeEach(() => {
    removeOfferMsw = [];

    mswServer.use(
      olxHttpMsw.delete('/api/offers/{offerId}', ({ request }) => {
        removeOfferMsw.push(request);
        return HttpResponse.json(null);
      }),
    );
  });

  test('removes offer, invalidates offer and redirects to offer page', async () => {
    const offerId = 123123;
    const queryClient = new QueryClient();
    const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries');

    const user = userEvent.setup();
    const { router } = await renderApp(
      { to: '/offers/$offerId', params: { offerId: `${offerId}` } },
      { queryClient },
    );

    await user.click(screen.getByRole('button', { name: 'Remove offer' }));

    expect(
      screen.getByRole('dialog', { name: 'Are you absolutely sure?' }),
    ).toBeVisible();

    await user.click(screen.getByRole('button', { name: 'Delete Offer' }));

    expect(
      await screen.findByText(/Your offer has been removed./i),
    ).toBeVisible();

    expect(removeOfferMsw).toHaveLength(1);
    expect(removeOfferMsw[0].url).toContain(`/api/offers/${offerId}`);

    expect(invalidateQueriesSpy).toHaveBeenCalledWith({
      queryKey: ['get', '/api/offers'],
    });
    expect(router.state.location.href).toBe('/offers');

    expect(
      screen.queryByRole('dialog', { name: 'Are you absolutely sure?' }),
    ).not.toBeInTheDocument();
  });

  test('opens dialog and closes it with cancel button', async () => {
    const user = userEvent.setup();
    await renderComponentWithRouterAndProviders(
      <RemoveOfferDialog offerId={123123} />,
    );

    await user.click(screen.getByRole('button', { name: 'Remove offer' }));

    expect(
      screen.getByRole('dialog', { name: 'Are you absolutely sure?' }),
    ).toBeVisible();

    await user.click(screen.getByRole('button', { name: 'Cancel' }));

    expect(
      screen.queryByRole('dialog', { name: 'Are you absolutely sure?' }),
    ).not.toBeInTheDocument();
    expect(removeOfferMsw).toHaveLength(0);
  });
});
