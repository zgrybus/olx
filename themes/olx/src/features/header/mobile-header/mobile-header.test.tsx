import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { renderApp } from '@/test/test-utils';
import { useIsDesktop } from '@/features/match-media-utils/use-match-media/use-match-media';

describe('Mobile Nav', () => {
  beforeEach(() => {
    vi.mocked(useIsDesktop).mockReturnValue({ isDesktop: false });
  });

  describe('Navigation', () => {
    test('redirects to home page, when we click on search button', async () => {
      const user = userEvent.setup();
      const { router } = await renderApp({ to: '/offers' });

      expect(router.state.location.href).toBe('/offers');

      await user.click(screen.getByRole('link', { name: 'Search' }));

      expect(router.state.location.href).toBe('/');
    });

    test('redirects to add offer page, when we click on add offer button', async () => {
      const user = userEvent.setup();
      const { router } = await renderApp({ to: '/' });

      expect(router.state.location.href).toBe('/');

      await user.click(screen.getByRole('link', { name: 'Add offer' }));

      expect(router.state.location.href).toBe('/offers');
    });
  });
});
