import { Outlet, createRootRouteWithContext } from '@tanstack/react-router';

import type { QueryClient } from '@tanstack/react-query';

type MyRouterContext = {
  queryClient: QueryClient;
};

export const Route = createRootRouteWithContext<MyRouterContext>()({
  component: () => (
    <div className="bg-slate-100 min-h-dvh w-full bg-slate-10 font-sans text-neutral-900">
      <Outlet />
    </div>
  ),
});
