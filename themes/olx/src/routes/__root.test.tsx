import { screen } from '@testing-library/react';
import { useIsDesktop } from '@/features/match-media-utils/use-match-media/use-match-media';
import { renderApp } from '@/test/test-utils';

describe('Root Page', () => {
  test('renders desktop header', async () => {
    vi.mocked(useIsDesktop).mockReturnValue({ isDesktop: true });

    await renderApp({ to: '/' });

    expect(screen.getByTestId('desktop-header')).toBeVisible();
    expect(screen.queryByTestId('mobile-navigation')).not.toBeInTheDocument();
  });

  test('renders mobile header', async () => {
    vi.mocked(useIsDesktop).mockReturnValue({ isDesktop: false });

    await renderApp({ to: '/' });

    expect(screen.getByTestId('mobile-navigation')).toBeVisible();
    expect(screen.queryByTestId('desktop-navigation')).not.toBeInTheDocument();
  });
});
