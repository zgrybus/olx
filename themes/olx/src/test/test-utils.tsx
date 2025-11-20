import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { render } from '@testing-library/react';
import {
  RouterProvider,
  createMemoryHistory,
  createRootRoute,
  createRouter,
  defaultStringifySearch,
  interpolatePath,
} from '@tanstack/react-router';
import { act } from 'react';
import type { ReactNode } from 'react';
import type {
  RegisteredRouter,
  ValidateNavigateOptions,
} from '@tanstack/react-router';
import { routeTree } from '@/routeTree.gen';

const interpolateRoute = <
  TOptions,
  TRouter extends RegisteredRouter = RegisteredRouter,
>(
  navigate: ValidateNavigateOptions<TRouter, TOptions>,
) => {
  const path = interpolatePath({
    path: navigate.to as string,
    params: navigate.params as Record<string, unknown>,
  });

  return (
    path.interpolatedPath +
    defaultStringifySearch(navigate.search as Record<string, unknown>)
  );
};

const createTestQueryClient = () =>
  new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });

function createTestRouter(component: ReactNode) {
  const rootRoute = createRootRoute({
    component: () => <>{component}</>,
  });

  return createRouter({
    routeTree: rootRoute,
    history: createMemoryHistory(),
  });
}

type TestProvidersProps = {
  children: ReactNode;
  queryClient?: QueryClient;
};

function TestProviders({
  queryClient = createTestQueryClient(),
  children,
}: TestProvidersProps) {
  return (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
}

type RenderWithProvidersOptions = {
  queryClient: QueryClient;
};

export function renderComponentWithProviders(
  ui: ReactNode,
  options?: RenderWithProvidersOptions,
) {
  const testQueryClient = options?.queryClient ?? createTestQueryClient();
  const router = createTestRouter(ui);

  const { rerender, ...result } = render(
    <TestProviders queryClient={testQueryClient}>
      <RouterProvider router={router} />
    </TestProviders>,
  );

  return {
    ...result,
    router,
    rerender: (rerenderUi: ReactNode) => {
      const newRouter = createTestRouter(rerenderUi);
      rerender(
        <TestProviders queryClient={testQueryClient}>
          <RouterProvider router={newRouter} />
        </TestProviders>,
      );
    },
  };
}

type RenderAppOptions = {
  queryClient?: QueryClient;
};

export async function renderApp<
  TOptions,
  TRouter extends RegisteredRouter = RegisteredRouter,
>(
  navigate: ValidateNavigateOptions<TRouter, TOptions>,
  options?: RenderAppOptions,
) {
  const queryClient = options?.queryClient ?? createTestQueryClient();

  const router = createRouter({
    routeTree,
    context: {
      queryClient,
    },
    history: createMemoryHistory({
      initialEntries: [interpolateRoute(navigate)],
    }),
  });

  const result = render(
    <TestProviders queryClient={queryClient}>
      <RouterProvider router={router} />
    </TestProviders>,
  );

  await act(() => router.navigate(navigate));

  const rerender = async () => {
    result.rerender(<RouterProvider router={router} />);
    await act(async () => {});
  };

  return await act(
    async () =>
      await {
        ...result,
        router,
        rerender,
      },
  );
}
