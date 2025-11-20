import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from '@/components/ui/card';
import { Skeleton } from '@/components/ui/skeleton';
import { Separator } from '@/components/ui/separator';

export function OfferDetailsSkeleton() {
  return (
    <div
      className="flex flex-col gap-3 md:flex-row mt-6"
      data-testid="offer-details-skeleton"
    >
      <div className="md:w-1/3 md:order-2">
        <Card>
          <CardContent className="space-y-4">
            <Skeleton className="h-3 w-32" />
            <div className="space-y-2">
              <Skeleton className="h-7 w-full" />
              <Skeleton className="h-7 w-2/3" />
            </div>
            <Skeleton className="h-9 w-40 rounded-md" />
          </CardContent>
        </Card>
      </div>
      <div className="md:w-2/3">
        <Card>
          <CardHeader>
            <Skeleton className="h-6 w-32" />
          </CardHeader>
          <CardContent className="space-y-3">
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-[92%]" />
            <Skeleton className="h-4 w-[98%]" />
            <Skeleton className="h-4 w-[85%]" />
            <Skeleton className="h-4 w-[90%]" />
            <Skeleton className="h-4 w-[60%]" />
          </CardContent>
          <Separator className="my-2" />
          <CardFooter>
            <Skeleton className="h-3 w-24" />
          </CardFooter>
        </Card>
      </div>
    </div>
  );
}
