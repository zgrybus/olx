import { Outlet, createRootRouteWithContext } from '@tanstack/react-router';

import type { QueryClient } from '@tanstack/react-query';

type MyRouterContext = {
  queryClient: QueryClient;
};

export const Route = createRootRouteWithContext<MyRouterContext>()({
  component: () => (
    <div className="min-h-dvh min-w-dvh bg-zinc-100">
      <Outlet />
    </div>
  ),
});
