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
            `
              flex h-[120px] flex-col justify-between
              md:h-40
            `,
            'transition-all duration-200 ease-in-out',
            `
              border-neutral-200/60
              hover:bg-neutral-50 hover:shadow-md
            `,
            'cursor-pointer',
          )}
        >
          <CardContent className="flex h-full flex-col gap-2">
            <div className="flex items-start justify-between gap-3">
              <h3
                className={`
                  line-clamp-2 min-w-0 text-base leading-tight font-medium
                  text-neutral-900
                  group-hover:underline
                `}
              >
                {title}
              </h3>
              <p
                className={`
                  shrink-0 text-base font-bold whitespace-nowrap
                  text-neutral-900
                `}
              >
                {priceFormatUtil.format(price)}
              </p>
            </div>
            <div
              className={`
                hidden flex-1 overflow-hidden
                md:block
              `}
            >
              <p
                className={`
                  line-clamp-2 text-sm leading-relaxed text-muted-foreground
                `}
              >
                {description}
              </p>
            </div>
          </CardContent>
          <CardFooter
            className={`
              mt-auto flex flex-col items-start gap-0.5 pt-0 text-xs
              text-muted-foreground
              md:flex-row md:gap-4
            `}
          >
            <p>
              <span className="font-medium">Updated:</span>{' '}
              {dateTimeFormat.format(new Date(updatedAt))}
            </p>
            <p
              className={`
                hidden
                md:block
              `}
            >
              <span className="font-medium">Created:</span>{' '}
              {dateTimeFormat.format(new Date(createdAt))}
            </p>
          </CardFooter>
        </Card>
      </Link>
    </li>
  );
}
