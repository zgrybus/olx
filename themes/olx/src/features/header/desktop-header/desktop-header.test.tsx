import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { renderApp } from '@/test/test-utils';

describe('Desktop Header', () => {
  describe('Navigation', () => {
    test('redirects to home page, when we click on logo', async () => {
      const user = userEvent.setup();
      const { router } = await renderApp({ to: '/offers' });

      expect(router.state.location.href).toBe('/offers');

      await user.click(screen.getByRole('link', { name: 'OLX' }));

      expect(router.state.location.href).toBe('/');
    });

    test('redirects to offers page, when we click on add offer button', async () => {
      const user = userEvent.setup();
      const { router } = await renderApp({ to: '/' });

      expect(router.state.location.href).toBe('/');

      await user.click(screen.getByRole('link', { name: 'Add offer' }));

      expect(router.state.location.href).toBe('/offers');
    });
  });
});
