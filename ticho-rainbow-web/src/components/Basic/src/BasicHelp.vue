<script lang="tsx">
  import type { CSSProperties, PropType, VNodeChild } from 'vue';
  import { computed, defineComponent, unref } from 'vue';
  import { Tooltip } from 'ant-design-vue';
  import { InfoCircleOutlined } from '@ant-design/icons-vue';
  import { getPopupContainer } from '@/utils';
  import { isArray, isString } from '@/utils/is';
  import { getSlot } from '@/utils/helper/tsxHelper';
  import { useDesign } from '@/hooks/web/useDesign';

  const props = {
    /**
     * Help text max-width
     * @default: 600px
     */
    maxWidth: { type: String, default: '600px' },
    /**
     * Whether to display the serial number
     * @default: false
     */
    showIndex: { type: Boolean },
    /**
     * Help text font color
     * @default: #ffffff
     */
    color: { type: String, default: '#ffffff' },
    /**
     * Help text font size
     * @default: 14px
     */
    fontSize: { type: String, default: '14px' },
    /**
     * Help text list
     */
    placement: { type: String, default: 'right' },
    /**
     * Help text list
     */
    text: {
      type: [Array, String, Object] as PropType<string[] | string | VNodeChild | JSX.Element>,
    },
  };

  export default defineComponent({
    name: 'BasicHelp',
    components: { Tooltip },
    props,
    setup(props, { slots }) {
      const { prefixCls } = useDesign('basic-help');

      const getTooltipStyle = computed(
        (): CSSProperties => ({ color: props.color, fontSize: props.fontSize }),
      );

      const getOverlayStyle = computed((): CSSProperties => ({ maxWidth: props.maxWidth }));

      function renderTitle() {
        const textList = props.text;

        if (isString(textList)) {
          return <p>{textList}</p>;
        }

        if (isArray(textList)) {
          return textList.map((text, index) => {
            return (
              <p key={text}>
                <>
                  {props.showIndex ? `${index + 1}. ` : ''}
                  {text}
                </>
              </p>
            );
          });
        }
        return <div>{textList}</div>;
      }

      return () => {
        return (
          <Tooltip
            overlayClassName={`${prefixCls}__wrap`}
            title={<div style={unref(getTooltipStyle)}>{renderTitle()}</div>}
            autoAdjustOverflow={true}
            overlayStyle={unref(getOverlayStyle)}
            placement={props.placement as 'right'}
            getPopupContainer={() => getPopupContainer()}
          >
            <span class={prefixCls}>{getSlot(slots) || <InfoCircleOutlined />}</span>
          </Tooltip>
        );
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-basic-help';

  .@{prefix-cls} {
    display: inline-block;
    margin-left: 6px;
    color: @text-color-help-dark;
    font-size: 14px;
    cursor: pointer;

    &:hover {
      color: @primary-color;
    }

    &__wrap {
      p {
        margin-bottom: 0;
      }
    }
  }
</style>
