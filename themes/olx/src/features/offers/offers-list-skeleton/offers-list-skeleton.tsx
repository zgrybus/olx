import { Skeleton } from '@/components/ui/skeleton';

export function OffersListSkeleton() {
  return (
    <div data-testid="offers-list-skeleton">
      <Skeleton className="h-7 w-48 rounded-md" />
      <ul className="mt-8 flex flex-col gap-4">
        {Array.from({ length: 5 }).map((_, index) => (
          <li
            key={index}
            className={`
              flex h-[120px] flex-col justify-between bg-white p-2 shadow-sm
              md:h-40
            `}
          >
            <div className="flex justify-between gap-3">
              <div className="w-full max-w-[70%] space-y-2">
                <Skeleton className="h-5 w-full" />
                <Skeleton
                  className={`
                    h-5 w-2/3
                    md:hidden
                  `}
                />
              </div>
              <Skeleton className="h-6 w-20 shrink-0" />
            </div>

            <div
              className={`
                mt-5 mb-3 hidden flex-1 space-y-2
                md:block
              `}
            >
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/5" />
            </div>

            <div className="mt-auto space-y-1.5 pt-2">
              <div className="flex items-center gap-2">
                <Skeleton className="h-3 w-16" />
                <Skeleton className="h-3 w-24" />
              </div>
              <div className="flex items-center gap-2">
                <Skeleton className="h-3 w-16" />
                <Skeleton className="h-3 w-24" />
              </div>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
