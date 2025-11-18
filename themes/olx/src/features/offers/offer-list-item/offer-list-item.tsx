import { Link } from '@tanstack/react-router';
import type { OfferSummary } from '../types';
import { dateTimeFormatUtil } from '@/features/date-utils/date-time-format-util';
import { priceFormatUtil } from '@/features/number-utils/number-format-util';
import { Card, CardContent, CardFooter } from '@/components/ui/card';
import { cn } from '@/lib/utils';

const dateTimeFormat = dateTimeFormatUtil({
  day: 'numeric',
  month: 'long',
  year: 'numeric',
});

type OfferListItemProps = OfferSummary;

export function OfferListItem({
  id,
  title,
  price,
  updatedAt,
  createdAt,
  description,
}: OfferListItemProps) {
  return (
    <li className="list-none">
      <Link
        to="/offers/$offerId"
        params={{ offerId: id.toString() }}
        className="block h-full"
      >
        <Card
          className={cn(
            'flex flex-col justify-between h-[120px] md:h-40',
            'transition-all duration-200 ease-in-out',
            'hover:bg-neutral-50 hover:shadow-md border-neutral-200/60',
            'cursor-pointer',
          )}
        >
          <CardContent className="flex flex-col h-full gap-2">
            <div className="flex justify-between gap-3 items-start">
              <h3 className="text-base font-medium leading-tight line-clamp-2 text-neutral-900 group-hover:underline min-w-0">
                {title}
              </h3>
              <p className="text-base font-bold whitespace-nowrap shrink-0 text-neutral-900">
                {priceFormatUtil.format(price)}
              </p>
            </div>
            <div className="hidden md:block flex-1 overflow-hidden">
              <p className="text-sm text-muted-foreground line-clamp-2 leading-relaxed">
                {description}
              </p>
            </div>
          </CardContent>
          <CardFooter className="pt-0 mt-auto text-xs text-muted-foreground flex flex-col items-start gap-0.5 md:flex-row md:gap-4">
            <p>
              <span className="font-medium">Updated:</span>{' '}
              {dateTimeFormat.format(new Date(updatedAt))}
            </p>
            <p className="hidden md:block">
              <span className="font-medium">Created:</span>{' '}
              {dateTimeFormat.format(new Date(createdAt))}
            </p>
          </CardFooter>
        </Card>
      </Link>
    </li>
  );
}
