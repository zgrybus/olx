import { Skeleton } from '@/components/ui/skeleton';

export function OffersListSkeleton() {
  return (
    <div>
      <Skeleton className="h-7 w-48 rounded-md" />
      <ul className="flex flex-col gap-4 mt-8">
        {Array.from({ length: 5 }).map((_, index) => (
          <li
            key={index}
            className="bg-white p-2 h-[120px] shadow-sm flex flex-col justify-between md:h-40"
          >
            <div className="flex justify-between gap-3">
              <div className="space-y-2 w-full max-w-[70%]">
                <Skeleton className="h-5 w-full" />
                <Skeleton className="h-5 w-2/3 md:hidden" />
              </div>
              <Skeleton className="h-6 w-20 shrink-0" />
            </div>

            <div className="hidden md:block mt-5 mb-3 flex-1 space-y-2">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/5" />
            </div>

            <div className="mt-auto pt-2 space-y-1.5">
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
