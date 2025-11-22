import { Outlet, createRootRouteWithContext } from '@tanstack/react-router';
import { Toaster } from 'sonner';
import type { QueryClient } from '@tanstack/react-query';
import { MobileNav } from '@/features/header/mobile-header/mobile-header';
import { DesktopHeader } from '@/features/header/desktop-header/desktop-header';
import { useIsDesktop } from '@/features/match-media-utils/use-match-media/use-match-media';

type MyRouterContext = {
  queryClient: QueryClient;
};

export const Route = createRootRouteWithContext<MyRouterContext>()({
  component: RootComponent,
});

function RootComponent() {
  const { isDesktop } = useIsDesktop();

  return (
    <div className="bg-slate-100 min-h-dvh w-full bg-slate-10 font-sans text-neutral-900">
      <Toaster position="top-center" richColors />
      {isDesktop && <DesktopHeader />}
      <Outlet />
      {!isDesktop && <MobileNav />}
    </div>
  );
}
